package com.accloud.murata_android.controller;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.murata_android.config.AppConstant;
import com.accloud.murata_android.config.Config;
import com.accloud.murata_android.config.ConstantCache;
import com.accloud.service.ACDeviceMsg;
import com.accloud.service.ACObject;

/**
 * Created by Xuri on 2015/1/24.
 */
public class SendToDevice {

    public void openDevice(String detailName, PayloadCallback<ACDeviceMsg> callback) {
        /**
         * 通过云端服务往设备发送命令/消息
         *
         * @param subDomain 子域名，如glass（智能眼镜）
         * @param deviceId  设备逻辑id
         * @param msg       具体的消息内容
         *
         * @return 设备返回的监听回调，返回设备的响应消息
         */
        AC.bindMgr().sendToDeviceWithOption(Config.SUBMAJORDOMAIN, ConstantCache.physicalDeviceId, getACDeviceMsg(detailName, 1), AC.ONLY_CLOUD, callback);
    }

    public void closeDevice(String detailName, PayloadCallback<ACDeviceMsg> callback) {
        /**
         * 通过云端服务往设备发送命令/消息
         *
         * @param subDomain 子域名，如glass（智能眼镜）
         * @param deviceId  设备逻辑id
         * @param msg       具体的消息内容
         *
         * @return 设备返回的监听回调，返回设备的响应消息
         */
        AC.bindMgr().sendToDeviceWithOption(Config.SUBMAJORDOMAIN, ConstantCache.physicalDeviceId, getACDeviceMsg(detailName, 0), AC.ONLY_CLOUD, callback);
    }

    public void setLEDColor(int red, int green, int blue, PayloadCallback<ACDeviceMsg> callback) {
        ACObject object = new ACObject();
        object.put("code", 102);
        object.put("uuid", "MIDB1");
        object.put("serial", 8900);
        ACObject redStream = new ACObject();
        redStream.put("stream_id", AppConstant.STREAMID_LED_RED);
        redStream.put("value", red);
        object.add("streams", redStream);
        ACObject greenStream = new ACObject();
        greenStream.put("stream_id", AppConstant.STREAMID_LED_GREEN);
        greenStream.put("value", green);
        object.add("streams", greenStream);
        ACObject blueStream = new ACObject();
        blueStream.put("stream_id", AppConstant.STREAMID_LED_BLUE);
        blueStream.put("value", blue);
        object.add("streams", blueStream);
        AC.bindMgr().sendToDeviceWithOption(Config.SUBMAJORDOMAIN, ConstantCache.physicalDeviceId, new ACDeviceMsg(Config.LIGHT_MSGCODE, object), AC.ONLY_CLOUD, callback);
    }

    public void setMotoSpeed(int value, PayloadCallback<ACDeviceMsg> callback) {
        /**
         * 通过云端服务往设备发送命令/消息
         *
         * @param subDomain 子域名，如glass（智能眼镜）
         * @param deviceId  设备逻辑id
         * @param msg       具体的消息内容
         *
         * @return 设备返回的监听回调，返回设备的响应消息
         */
        AC.bindMgr().sendToDeviceWithOption(Config.SUBMAJORDOMAIN, ConstantCache.physicalDeviceId, getACDeviceMsg(AppConstant.STREAMID_MOTO_PWM, value), AC.ONLY_CLOUD, callback);
    }

    private ACDeviceMsg getACDeviceMsg(String name, int value) {
        ACObject object = new ACObject();
        object.put("code", 102);
        object.put("uuid", "MIDB1");
        object.put("serial", 8900);

        ACObject stream = new ACObject();

        switch (name) {
            case AppConstant.STREAMID_BUZZER:
                stream.put("stream_id", AppConstant.STREAMID_BUZZER);
                stream.put("value", value);
                object.add("streams", stream);
                break;
            case AppConstant.STREAMID_LIGHT_1:
                stream.put("stream_id", AppConstant.STREAMID_LIGHT_1);
                stream.put("value", value);
                object.add("streams", stream);
                break;
            case AppConstant.STREAMID_LIGHT_2:
                stream.put("stream_id", AppConstant.STREAMID_LIGHT_2);
                stream.put("value", value);
                object.add("streams", stream);
                break;
            case AppConstant.STREAMID_LED:
                stream.put("stream_id", AppConstant.STREAMID_LED);
                stream.put("value", value);
                object.add("streams", stream);
                break;
            case AppConstant.STREAMID_MOTO:
                stream.put("stream_id", AppConstant.STREAMID_MOTO);
                stream.put("value", value);
                object.add("streams", stream);
                break;
            case AppConstant.STREAMID_MOTO_PWM:
                stream.put("stream_id", AppConstant.STREAMID_MOTO_PWM);
                stream.put("value", value);
                object.add("streams", stream);
                break;
        }
        return new ACDeviceMsg(Config.LIGHT_MSGCODE, object);
    }
}
