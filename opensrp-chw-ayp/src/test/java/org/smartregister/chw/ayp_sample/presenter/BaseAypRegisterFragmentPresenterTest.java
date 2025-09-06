package org.smartregister.chw.ayp_sample.presenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.smartregister.chw.ayp.contract.aypRegisterFragmentContract;
import org.smartregister.chw.ayp.presenter.BaseAypRegisterFragmentPresenter;
import org.smartregister.chw.ayp.contract.AypRegisterFragmentContract;
import org.smartregister.chw.ayp.util.Constants;
import org.smartregister.chw.ayp.util.DBConstants;
import org.smartregister.configurableviews.model.View;

import java.util.Set;
import java.util.TreeSet;

public class BaseAypRegisterFragmentPresenterTest {
    @Mock
    protected AypRegisterFragmentContract.View view;

    @Mock
    protected AypRegisterFragmentContract.Model model;

    private BaseAypRegisterFragmentPresenter baseaypRegisterFragmentPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        baseaypRegisterFragmentPresenter = new BaseAypRegisterFragmentPresenter(view, model, "");
    }

    @Test
    public void assertNotNull() {
        Assert.assertNotNull(baseaypRegisterFragmentPresenter);
    }

    @Test
    public void getMainCondition() {
        Assert.assertEquals(" ec_ayp_enrollment.is_closed = 0 ", baseaypRegisterFragmentPresenter.getMainCondition());
    }

    @Test
    public void getDueFilterCondition() {
        Assert.assertEquals(" (cast( julianday(STRFTIME('%Y-%m-%d', datetime('now'))) -  julianday(IFNULL(SUBSTR(ayp_test_date,7,4)|| '-' || SUBSTR(ayp_test_date,4,2) || '-' || SUBSTR(ayp_test_date,1,2),'')) as integer) between 7 and 14) ", baseaypRegisterFragmentPresenter.getDueFilterCondition());
    }

    @Test
    public void getDefaultSortQuery() {
        Assert.assertEquals(Constants.TABLES.ayp_ENROLLMENT + "." + DBConstants.KEY.LAST_INTERACTED_WITH + " DESC ", baseaypRegisterFragmentPresenter.getDefaultSortQuery());
    }

    @Test
    public void getMainTable() {
        Assert.assertEquals(Constants.TABLES.ayp_ENROLLMENT, baseaypRegisterFragmentPresenter.getMainTable());
    }

    @Test
    public void initializeQueries() {
        Set<View> visibleColumns = new TreeSet<>();
        baseaypRegisterFragmentPresenter.initializeQueries(null);
        Mockito.doNothing().when(view).initializeQueryParams(Constants.TABLES.ayp_ENROLLMENT, null, null);
        Mockito.verify(view).initializeQueryParams(Constants.TABLES.ayp_ENROLLMENT, null, null);
        Mockito.verify(view).initializeAdapter(visibleColumns);
        Mockito.verify(view).countExecute();
        Mockito.verify(view).filterandSortInInitializeQueries();
    }

}
