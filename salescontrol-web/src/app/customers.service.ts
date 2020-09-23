import { Injectable } from '@angular/core';
import { Customer } from './customers/customer';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CustomersService {

  constructor( private http : HttpClient ) { 

  }

  apiURL: string = environment.apiUrlBase + '/api/customers';

  //Observable waits for the return of the http to act when it has the response of the server
  save(customer: Customer) : Observable<any>{
    return this.http.post<Customer>(this.apiURL , customer);
  }

  update(customer: Customer) : Observable<any>{
    return this.http.put<Customer>(`${this.apiURL}/${customer.id}`,customer);
  }

  delete(customer: Customer) : Observable<any>{
    return this.http.delete<Customer>(`${this.apiURL}/${customer.id}`);
  }

  getCustomersPageable(page : number, size : number) : Observable<Customer[]>{
    return this.http.get<Customer[]>(`${this.apiURL}?page=${page}&size=${size}`);
  }

  getCustomers() : Observable<Customer[]>{
    return this.http.get<Customer[]>(`${this.apiURL}/all`);
  }

  findCustomerById(id: number) : Observable<Customer>{
    return this.http.get<Customer>(`${this.apiURL}/${id}`);
  }

}
