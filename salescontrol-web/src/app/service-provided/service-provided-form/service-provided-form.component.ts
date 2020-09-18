import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Customer } from '../../customers/customer';
import { ServiceProvider } from '../service-provider';
import { CustomersService } from '../../customers.service';
import { ServiceProvidedService } from '../../service-provided.service';

@Component({
  selector: 'app-service-provided-form',
  templateUrl: './service-provided-form.component.html',
  styleUrls: ['./service-provided-form.component.css']
})

export class ServiceProvidedFormComponent implements OnInit {

  customers : Customer[] = [];
  success : boolean;
  errors : string [];
  serviceProvider: ServiceProvider;

  constructor(
    private customerService : CustomersService,
    private serviceProvidedService : ServiceProvidedService,
    private router : Router
  ) { 
    this.serviceProvider = new ServiceProvider();
  }

  ngOnInit(): void {

    this.customerService
      .getCustomers()
      .subscribe( response => this.customers = response);
  }

  onSubmit(){
    this.serviceProvidedService
      .salvar(this.serviceProvider)
      .subscribe( response => {
        console.log(response);
        this.serviceProvider = new ServiceProvider();
        this.success = true;
        this.errors = [];
      }, errorResponse => {
        console.log(errorResponse);
        this.success = false;
        this.errors = errorResponse.error;
      });

  }

  backToServiceProvidedList(){
    this.router.navigate(['/service-provided/list']);
  }
}
