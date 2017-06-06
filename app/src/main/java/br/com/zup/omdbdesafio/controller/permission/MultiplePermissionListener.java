package br.com.zup.omdbdesafio.controller.permission;

import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import br.com.zup.omdbdesafio.view.interfaces.ShowPermissions;


/**
 * Created by pedro on 25/01/17.
 */

public class MultiplePermissionListener implements MultiplePermissionsListener {

    private final ShowPermissions showPermissions;

    public MultiplePermissionListener(ShowPermissions showPermissions) {
        this.showPermissions = showPermissions;
    }


    @Override
    public void onPermissionsChecked(MultiplePermissionsReport report) {

        if(report.getDeniedPermissionResponses().size() == 0)
            showPermissions.showPermissionGranted("REQUEST_PERMISSIONS_STORAGE_SUCCES");
        else
            showPermissions.showPermissionDenied("REQUEST_PERMISSIONS_STORAGE_SUCCES", false);

    }

    @Override
    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
        token.continuePermissionRequest();
    }
}
