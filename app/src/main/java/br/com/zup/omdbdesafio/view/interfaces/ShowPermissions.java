package br.com.zup.omdbdesafio.view.interfaces;

/**
 *
 */

public interface ShowPermissions {
    void showPermissionGranted(String permission);
    void showPermissionDenied(String permission, boolean isPermanentlyDenied);
}
