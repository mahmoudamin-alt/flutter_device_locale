package dev.leadcode.flutter_device_locale;

import android.content.res.Resources;
import android.os.LocaleList;

import androidx.annotation.NonNull;

import java.util.*;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

public class FlutterDeviceLocalePlugin implements FlutterPlugin, MethodCallHandler {
    private MethodChannel channel;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        register(flutterPluginBinding.getBinaryMessenger());
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        if (channel != null) {
            channel.setMethodCallHandler(null);
            channel = null;
        }
    }

    private void register(BinaryMessenger binaryMessenger) {
        channel = new MethodChannel(binaryMessenger, "flutter_device_locale");
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if ("deviceLocales".equals(call.method)) {
            result.success(getDeviceLocales());
        } else {
            result.notImplemented();
        }
    }

    private List<String> getDeviceLocales() {
        List<String> result = new ArrayList<>();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            LocaleList list = Resources.getSystem().getConfiguration().getLocales();
            for (int i = 0; i < list.size(); i++) {
                result.add(list.get(i).toString());
            }
        } else {
            result.add(Locale.getDefault().toString());
        }

        return result;
    }
}
