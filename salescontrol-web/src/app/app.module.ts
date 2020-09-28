import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule } from '@angular/forms'
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CustomersModule } from './customers/customers.module';
import { ServiceProvidedModule } from './service-provided/service-provided.module';
import { TemplateModule } from './template/template.module';
import { CustomersService } from './customers.service';
import { ServiceProvidedService } from './service-provided.service';
import { AuthService } from './auth.service';
import { LoginComponent } from './login/login.component';
import { LayoutComponent } from './layout/layout.component';
import { TokenInterceptor } from './token.interceptor';
import { CurrencyMaskModule } from 'ng2-currency-mask';
import { NgxPaginationModule } from 'ngx-pagination';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    LayoutComponent,

  ],
  imports: [
    BrowserModule,
    FormsModule,
    CurrencyMaskModule,
    AppRoutingModule,
    HttpClientModule,
    TemplateModule,
    CustomersModule,
    ServiceProvidedModule,
    NgxPaginationModule,
  ],
  //providers: Dependency injection
  providers: [
    CustomersService,
    ServiceProvidedService,
    AuthService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
