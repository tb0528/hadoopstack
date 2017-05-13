package com.xiaoxiaomo.hbase.pro.download;

import com.xxo.pro.domain.Page;
import com.xxo.pro.utils.PageUtils;

/**
 * Created by xiaoxiaomo on 2015/5/3.
 */
public class DownloadJDPageImpl implements Downloadable {
    public Page download( String url) {
        Page page = new Page();
        page.setUrl( url );
        page.setContent(PageUtils.getContentByURL( url ));
        return page ;
    }
}
