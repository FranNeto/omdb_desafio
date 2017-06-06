package br.com.zup.omdbdesafio.controller.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

import br.com.zup.omdbdesafio.R;
import br.com.zup.omdbdesafio.controller.listener.IBackPressListener;
import br.com.zup.omdbdesafio.controller.listener.ICallFragmetsActivityListener;
import br.com.zup.omdbdesafio.controller.listener.IOmdbActivityListener;
import br.com.zup.omdbdesafio.view.Frags;
import br.com.zup.omdbdesafio.view.fragment.AbstractFragment;


public abstract class AbstractFragmentActivity extends AppCompatActivity implements ICallFragmetsActivityListener, IOmdbActivityListener {

    public FragmentManager fragMan = null;

    private Map<String, AbstractFragment> mapFrags = new HashMap<String, AbstractFragment>();

    protected IBackPressListener iBackPressListener;

    private boolean onPause;

    private Fragment frag;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        fragMan = getSupportFragmentManager();
    }

    public void registerBackPress(final IBackPressListener iBackPressListener) {
        this.iBackPressListener = iBackPressListener;
    }

    public IBackPressListener getBackPressListener() {
        return iBackPressListener;
    }

    @Override
    protected void onPause() {
        super.onPause();
        setOnPause(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setOnPause(false);
    }


    public AbstractFragment registerFragment(final Frags frag) {
        return registerFragment(frag, null);
    }

    public AbstractFragment registerFragment(final Frags frag, final String tagFragMan) {

        String tag = tagFragMan;
        if (tag == null) {
            tag = frag.getName();
        }

        final Fragment fragment = fragMan.findFragmentByTag(tag);

        AbstractFragment abstractFragment = null;

        if (fragment == null) {
            try {
                abstractFragment = frag.getClassFrag().newInstance();
            } catch (InstantiationException e) {
               Log.e("InstantiationException " + e.getMessage(), getClass().getName());
            } catch (IllegalAccessException e) {
                Log.e("IllegalAccessException " + e, getClass().getName());
            }
        } else {
            if (fragment instanceof AbstractFragment) {
                abstractFragment = (AbstractFragment) fragment;
            }
        }

        if (abstractFragment != null) {
            this.frag = abstractFragment;
            mapFrags.put(frag.getName(), abstractFragment);
        }

        return abstractFragment;
    }



    public void removeFragment(final Frags frag) {
        Fragment fragment = getFragment(frag);
        if(frag != null) {
            removeFragment(fragment);
        }
    }

    protected void removeFragment(Fragment fragment) {

        FragmentTransaction fragTransa = fragMan.beginTransaction();

        fragTransa.remove(fragment);

        fragTransa.commitAllowingStateLoss();
    }

    @Override
    public boolean replaceFragment(final Frags frag, final int resId) {
        return replaceFragment(frag, resId, true);
    }

    @Override
    public void replaceFragment(final Frags frag, final boolean addBackStack) {
        replaceFragment(frag, R.id.content_frame, addBackStack);
    }

    @Override
    public boolean replaceFragment(final Frags frag, final int resId, final boolean addBackStack) {
        return replaceFragment(frag, resId, addBackStack, null, null);
    }

    @Override
    public boolean replaceFragment(final Frags frag, final int resId, final boolean addBackStack, final Integer animResIdEnter, final Integer animResIdExit) {

        if (isOnPause()) {
            return false;
        }

        boolean ret = false;

        Fragment fragment = mapFrags.get(frag.getName());

        if (fragment != null) {

            FragmentTransaction fragTransa = fragMan.beginTransaction();

            // setting animation
            if (animResIdEnter != null && animResIdExit != null) {
                fragTransa.setCustomAnimations(animResIdEnter, animResIdExit);
            }

            fragTransa.replace(resId, fragment, frag.getName());


            // adding backstack
            if (addBackStack) {
                fragTransa.addToBackStack(frag.getName());
            }

            fragTransa.commitAllowingStateLoss();
            ret = true;
        }

        return ret;
    }

    public synchronized AbstractFragment getFragment(final Frags frag) {

        if (!mapFrags.containsKey(frag.getName())) {
            return null;
        }

        return mapFrags.get(frag.getName());
    }

    @Override
    public void onBackPressed() {
        if (iBackPressListener != null) {
            iBackPressListener.onBackPress();

//            if (iBackPressListener !=ç null && iBackPressListener.isFirstBack()) {
                super.onBackPressed();
//            }

        } else {
            super.onBackPressed();
        }
    }

    public boolean isOnPause() {
        return onPause;
    }

    public void setOnPause(final boolean onPause) {
        this.onPause = onPause;
    }

    public AbstractFragment getFrag() {

        AbstractFragment fragment = null;

        // REMOVED BY PREVENT (BAD CASTS OF OBJECT REFERENCES)
        // IN CASE OF NEW FRAGMENT VERSIONS IT WILL BECOME NECESSARY THE USE OF INSTANCE OF VALIDATION
        fragment = (AbstractFragment) frag;
        return fragment;
    }

    public void popBackStack() {
        try {
            fragMan.popBackStackImmediate();
        } catch (IllegalStateException ignored) {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }
    }

    public void popBackStack(Frags frags) {
        fragMan.popBackStackImmediate(frags.getName(), 0);
    }

    /**
     * Set argument in a fragment
     *
     * @param frag - frag to set arguments
     * @param args - arguments bundle
     * @return Return true if set arguments, false otherwise
     */
    public boolean putArguments(final Frags frag, final Bundle args) {
        boolean ret = false;
        Fragment fragment = this.frag;

        if (fragment != null && args != null) {
            fragment.setArguments(args);
            ret = true;
        }

        return ret;
    }

    public boolean findFragment(Frags frag) {
        if (fragMan.findFragmentByTag(frag.getName()) != null) {
            return true;

        }
        return false;
    }

    public String recentFragmentName() {
        if (fragMan.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry entry = fragMan.getBackStackEntryAt(fragMan.getBackStackEntryCount());
            return entry.getName();
        }
        return null;
    }

    public String lastFragmentName() {
        if (fragMan.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry entry = fragMan.getBackStackEntryAt(fragMan.getBackStackEntryCount() - 1);
            return entry.getName();
        }
        return null;
    }

    public AbstractFragment lastFragmentFrags() {
        String nameFrag = lastFragmentName();
        if (nameFrag != null) {
            return mapFrags.get(nameFrag);
        }
        return null;
    }

    public void popBackStackStart() {
        try {
            fragMan.popBackStackImmediate(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } catch (IllegalStateException ignored) {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }
    }

    @Override
    public AbstractFragment getAFragment(Frags frag){
        return getFragment(frag);
    }
}
