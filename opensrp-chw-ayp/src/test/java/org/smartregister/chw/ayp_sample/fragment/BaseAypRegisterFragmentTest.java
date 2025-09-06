package org.smartregister.chw.ayp_sample.fragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;
import org.smartregister.chw.ayp.activity.BaseAypProfileActivity;
import org.smartregister.chw.ayp.fragment.BaseAypRegisterFragment;
import org.smartregister.commonregistry.CommonPersonObjectClient;

import static org.mockito.Mockito.times;

public class BaseAypRegisterFragmentTest {
    @Mock
    public BaseAypRegisterFragment baseTestRegisterFragment;

    @Mock
    public CommonPersonObjectClient client;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = Exception.class)
    public void openProfile() throws Exception {
        Whitebox.invokeMethod(baseTestRegisterFragment, "openProfile", client);
        PowerMockito.mockStatic(BaseAypProfileActivity.class);
        BaseAypProfileActivity.startProfileActivity(null, null);
        PowerMockito.verifyStatic(BaseAypProfileActivity.class, times(1));
        BaseAypProfileActivity.startProfileActivity(null, null);
    }
}
