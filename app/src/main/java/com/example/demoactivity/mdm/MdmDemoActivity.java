package com.example.demoactivity.mdm;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demoactivity.R;
import com.huawei.android.util.NoExtAPIException;

import ga.mdm.PolicyManager;

public class MdmDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mdm_demo);
        mdmDemo();
    }

    private void mdmDemo() throws NoExtAPIException {
        try {
//            DevicePolicyManager devicePolicyManager = DevicePolicyManager.getInstance(this);
//            PeripheralPolicy policy = devicePolicyManager.getPeripheralPolicy();
//            policy.enableWifi(false);

            PolicyManager policyManager = PolicyManager.getInstance();
            int mode = policyManager.getNfcPolicies();
            Toast.makeText(this, "NFC Policy is = " + mode, Toast.LENGTH_SHORT).show();
        } catch (NoExtAPIException e) {
            Toast.makeText(this, "当前设备为非定制设备！", Toast.LENGTH_SHORT).show();
        }

    }


}
