package vn.co.honda.hondacrm.di.application;

import javax.inject.Singleton;

import dagger.Component;
import vn.co.honda.hondacrm.App;
import vn.co.honda.hondacrm.di.home.HomeComponent;
import vn.co.honda.hondacrm.di.home.HomeModule;
import vn.co.honda.hondacrm.di.news.NewsComponent;
import vn.co.honda.hondacrm.di.news.NewsModule;
import vn.co.honda.hondacrm.di.newsDetail.NewsDetailedComponent;
import vn.co.honda.hondacrm.di.newsDetail.NewsDetailedModule;
import vn.co.honda.hondacrm.di.users.UsersComponent;
import vn.co.honda.hondacrm.di.users.UsersModule;
import vn.co.honda.hondacrm.ui.adapters.news.NewsHolder;

/**
 * @author CuongNV31
 */
@Component(modules = {AppModule.class, NetworkModule.class})
@Singleton
public interface AppComponent {

    NewsComponent plus(NewsModule module);

    NewsDetailedComponent plus(NewsDetailedModule module);

    UsersComponent plus(UsersModule module);

    HomeComponent plus(HomeModule module);

    void inject(App application);

    void inject(NewsHolder holder);

}
