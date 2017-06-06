package br.com.zup.omdbdesafio.controller.permission;

import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import br.com.zup.omdbdesafio.view.interfaces.ShowPermissions;


public class SamplePermissionListener implements PermissionListener {

  private final ShowPermissions showPermissions;

  public SamplePermissionListener(ShowPermissions showPermissions) {
    this.showPermissions = showPermissions;
  }

  @Override
  public void onPermissionGranted(PermissionGrantedResponse response) {
    showPermissions.showPermissionGranted(response.getPermissionName());
  }

  @Override
  public void onPermissionDenied(PermissionDeniedResponse response) {
    showPermissions.showPermissionDenied(response.getPermissionName(), response.isPermanentlyDenied());
  }

  @Override
  public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
    token.continuePermissionRequest();
  }
}
