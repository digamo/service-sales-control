import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { Customer } from '../customer';
import { CustomersService } from '../../customers.service';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-customers-form',
  templateUrl: './customers-form.component.html',
  styleUrls: ['./customers-form.component.css']
})
export class CustomersFormComponent implements OnInit {

  id: number;
  customer: Customer;
  success: boolean = false;
  errors: string[];
  customerJustBeenInserted = false;

  constructor( 
    private service : CustomersService,
    private router : Router,
    private activatedRoute: ActivatedRoute) { 
    this.customer = new Customer();
  }
  
  ngOnInit(): void {

    let params : Observable<Params> = this.activatedRoute.params;
    params.subscribe( urlParams => {
      this.id = urlParams['id'];
      if(this.id){
        this.service
          .findCustomerById(this.id)
          .subscribe( 
            response => this.customer = response,
            errorResponse => this.customer = new Customer()
              
          )
      }
    })
  }

  customersList(){
    this.router.navigate(['/customers/list']);
  }

  updateCustomer(){
      this.service
      .update(this.customer)
      .subscribe( response => {
        this.success = true;
        this.errors = [];
      }, errorResponse =>{
        this.errors = errorResponse.error;
        this.success = false;
      })
  }

  onSubmit(){

      this.service.save(this.customer)
      .subscribe( response =>{
        this.success = true;
        this.errors = [];
        this.customer = response;
        this.customerJustBeenInserted = true;
      }, errorResponse =>{
        this.customer = new Customer();
        this.success = false;
        this.errors = errorResponse.error;
      });

  }

  addCustomer(){
    this.customer = new Customer();
    this.success = false;
    this.router.navigate(['/customers/form']);
  }

}
