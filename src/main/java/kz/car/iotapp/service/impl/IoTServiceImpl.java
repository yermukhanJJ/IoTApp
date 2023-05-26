package kz.car.iotapp.service.impl;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.iot.AWSIot;
import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iot.AWSIotClientBuilder;
import com.amazonaws.services.iot.model.*;
import com.amazonaws.services.iotdata.AWSIotDataClient;
import com.amazonaws.services.iotdata.model.GetThingShadowRequest;
import com.amazonaws.services.iotdata.model.GetThingShadowResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.car.iotapp.model.dto.DeviceForm;
import kz.car.iotapp.model.dto.IoTDto;
import kz.car.iotapp.model.entity.Device;
import kz.car.iotapp.model.entity.IoT;
import kz.car.iotapp.model.entity.Vehicle;
import kz.car.iotapp.repository.DeviceRepository;
import kz.car.iotapp.repository.IoTRepository;
import kz.car.iotapp.repository.VehicleRepository;
import kz.car.iotapp.service.IoTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IoTServiceImpl implements IoTService {

    private final DeviceRepository deviceRepository;
    private final IoTRepository ioTRepository;
    private final VehicleRepository vehicleRepository;

    private String accessKey = "AKIAWCWFFDKJBLDH565D";
    private String privateKey = "kg+tBtnTp0INo4jqf4MQ7Q3TOCYKj13SdL1lU8bA";
    private static final String ENDPOINT = "a3dsbxr4nqe3oe-ats.iot.eu-north-1.amazonaws.com";

    @Override
    public void createIoT(Principal principal, DeviceForm form) {
        Vehicle vehicle = vehicleRepository.findByTitle(form.getVehicleType())
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST));
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, privateKey);
        AWSIotClient client = new AWSIotClient(credentials);
        client.setRegion(Region.getRegion(Regions.EU_NORTH_1));
        List<Device> devices = deviceRepository.findByTitleIn(form.getSelectedDeviceNames());
        List<IoT> ioTS = new ArrayList<>();
        for (Device device : devices) {
            CreateThingRequest request = new CreateThingRequest();
            request.setThingName(device.getTitle() + "_" + principal.getName() + "_" + vehicle.getId());
            AttributePayload attributePayload = new AttributePayload();
            attributePayload.addAttributesEntry(device.getAttribute(), String.valueOf(0));
            request.setAttributePayload(attributePayload);
            CreateThingResult result = client.createThing(request);
            IoT ioT = new IoT();
            ioT.setThingName(result.getThingName());
            ioT.setThingAttribute(device.getAttribute());
            ioT.setVehicle(vehicle);
            ioTS.add(ioT);
        }

        ioTRepository.saveAll(ioTS);
    }

    @Override
    public List<IoTDto> getAllIoTByVehicle(Long idVehicle) {
        Vehicle vehicle = vehicleRepository.findById(idVehicle)
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST));
        List<IoT> ioTList = ioTRepository.findAllByVehicle(vehicle);

        return ioTList.stream()
                .map(ioT -> (new IoTDto(
                        ioT.getId(),
                        ioT.getThingName(),
                        ioT.getThingAttribute(),
                        getAttributeValue(idVehicle, ioT.getId())
                ))).collect(Collectors.toList());
    }

    private List<String> getAttributeValues(Long idVehicle, Long iotId) {
        Vehicle vehicle = vehicleRepository.findById(idVehicle)
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST));
        IoT ioT = ioTRepository.findByVehicleAndId(vehicle, iotId)
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST));
        List<String> values = new ArrayList<>();

        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, privateKey);
        AWSIotDataClient client = new AWSIotDataClient(credentials);
        client.setRegion(Region.getRegion(Regions.EU_NORTH_1));
        client.setEndpoint(ENDPOINT);

        for (int i = 0; i < 6; i++) {
            try {

//                String attributePath = String.format("/things/%s/attributes/%s", ioT.getThingName(), ioT.getThingAttribute());
                GetThingShadowRequest getThingShadowRequest = new GetThingShadowRequest()
                        .withThingName(ioT.getThingName());

                GetThingShadowResult getThingShadowResult = client.getThingShadow(getThingShadowRequest);

                String payload = new String(getThingShadowResult.getPayload().array());
                JSONObject jsonObject = new JSONObject(payload);
                String attributeValue = jsonObject.getString(ioT.getThingAttribute());

                values.add(attributeValue);

                Thread.sleep(10000);
            } catch (Exception exception) {
                log.error("", exception);
            }
        }

        return values;
    }

    private String getAttributeValue(Long idVehicle, Long iotId) {
        Vehicle vehicle = vehicleRepository.findById(idVehicle)
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST));
        IoT ioT = ioTRepository.findByVehicleAndId(vehicle, iotId)
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST));

        AWSIot iotClient = AWSIotClient.builder()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, privateKey)))
                .withRegion(Regions.EU_NORTH_1)
                .build();

        DescribeThingRequest request = new DescribeThingRequest().withThingName(ioT.getThingName());
        DescribeThingResult result = iotClient.describeThing(request);
        Map<String, String> attributes = result.getAttributes();

        return attributes.get(ioT.getThingAttribute());

    }

}
