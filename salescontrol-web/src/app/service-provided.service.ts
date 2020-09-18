import { Injectable } from '@angular/core';
import { ServiceProvider } from './service-provided/service-provider';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { ServiceProviderSearch } from './service-provided/service-provided-list/service-provided-search.component';

@Injectable({
  providedIn: 'root'
})
export class ServiceProvidedService {

  constructor( private http : HttpClient ) { 

  }

  apiURL: string = environment.apiUrlBase + '/api/service-provided';

  salvar(serviceProvider: ServiceProvider) : Observable<any>{
    return this.http.post<ServiceProvider>(this.apiURL , serviceProvider);
  }

  getCustomers(customerName : string, serviceMonth: number) : Observable<ServiceProviderSearch[]>{

    const httpParams = new HttpParams()
      .set("customerName", customerName ? customerName : '')
      .set("serviceMonth", serviceMonth ? serviceMonth.toString(): '');

    return this.http.get<ServiceProviderSearch[]>(`${this.apiURL}?${httpParams.toString()}`);
  }
 
}
