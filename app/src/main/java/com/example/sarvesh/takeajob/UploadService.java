package com.example.sarvesh.takeajob;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class UploadService extends Service {
    public UploadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
