import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Customer } from '../customer';
import { CustomersService } from 'src/app/customers.service';

@Component({
  selector: 'app-customers-list',
  templateUrl: './customers-list.component.html',
  styleUrls: ['./customers-list.component.css']
})
export class CustomersListComponent implements OnInit {

  sizeOfListCustomer : number = 3;
  page : number = 0;
  pages : Array<number>; 
  customers: Customer[] = [];
  customerToDelete : Customer;
  messageSuccess : string;
  messageError : string;
  
  constructor( 
    private service : CustomersService,
    private router : Router ) { 

  }

  setPage(i : number, event:any){
    event.preventDefault();
    this.page = i;
    this.getCustomersPageable()
  }

  ngOnInit(): void {
    this.getCustomersPageable();
  }

  getCustomersPageable(){
    this.service.getCustomersPageable(this.page, this.sizeOfListCustomer).subscribe( 
      response => {
        this.customers = response['content'];
        this.pages = new Array(response['totalPages']);
    })
  }

  addCustomer(){
    this.router.navigate(['/customers/form']);
  }

  prepareToDeleteCustomer(customer : Customer){
    this.customerToDelete = customer;
  }

  deleteCustomers(){

    this.service
    .delete(this.customerToDelete)
    .subscribe( 
      response => {
        this.messageSuccess = "Cliente excluído com sucesso"
        this.getCustomersPageable();
      },
      errorResponse => this.messageError = "Erro ao deletar o cliente"
    )
  }


}
