package br.com.zup.omdbdesafio.controller.permission;

import com.karumi.dexter.Dexter;

import br.com.zup.omdbdesafio.view.interfaces.ShowPermissions;


/**
 *
 */

public class Permissions {

    public static void permissionsStorage(ShowPermissions showPermissions, String[] permissions){
        Dexter.checkPermissions(new MultiplePermissionListener(showPermissions), permissions);
    }

}
