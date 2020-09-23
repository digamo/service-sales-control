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

  save(serviceProvider: ServiceProvider) : Observable<any>{
    return this.http.post<ServiceProvider>(this.apiURL , serviceProvider);
  }

  getServiceProvided(customerName : string, serviceMonth: number) : Observable<ServiceProviderSearch[]>{

    const httpParams = new HttpParams()
      .set("customerName", customerName ? customerName : '')
      .set("serviceMonth", serviceMonth ? serviceMonth.toString(): '');

    return this.http.get<ServiceProviderSearch[]>(`${this.apiURL}?${httpParams.toString()}`);
  }
 
  getServiceProvidedPageable(customerName : string, serviceMonth: number, page : number, size : number) : Observable<ServiceProviderSearch[]>{

    const httpParams = new HttpParams()
    .set("customerName", customerName ? customerName : '')
    .set("serviceMonth", serviceMonth ? serviceMonth.toString(): '')
    .set("page", page > 0 ? page.toString() : '0')
    .set("size", size > 0 ? size.toString() : '4')
    ;

    //return this.http.get<ServiceProviderSearch[]>(`${this.apiURL}?page=${page}&size=${size}`);
    return this.http.get<ServiceProviderSearch[]>(`${this.apiURL}?${httpParams.toString()}`);
  }
  
}
