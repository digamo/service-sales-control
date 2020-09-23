import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ServiceProvidedService } from '../../service-provided.service';
import { ServiceProviderSearch } from './service-provided-search.component'

const getMonth = (idx) => {

  var objDate = new Date();
  objDate.setDate(1);
  objDate.setMonth(idx-1);

  var locale = "pt-br",
      month = objDate.toLocaleString(locale, { month: "long" });

    return month;
}

@Component({
  selector: 'app-service-provided-list',
  templateUrl: './service-provided-list.component.html',
  styleUrls: ['./service-provided-list.component.css']
})
export class ServiceProvidedListComponent implements OnInit {

  sizeOfListCustomer : number = 3;
  page : number = 0;
  pages : Array<number>; 

  customerName : string;
  serviceProvidedList : ServiceProviderSearch [];
  searchMessageReturn : string = ""
  emptyList : boolean = true
  
  month: number;
  months = Array(12).fill(0).map((i,idx) => getMonth(idx + 1));

  selectedMonth = 1;

  constructor(
    private router : Router,
    private service : ServiceProvidedService
  )
  { 

  }

  ngOnInit(): void {
    this.getServiceProvidedPageable();
  } 

  setPage(i : number, event:any){
    event.preventDefault();
    this.page = i;
    this.getServiceProvidedPageable()
  }

  getServiceProvidedPageable(){
    console.log("pagable");
    this.service.getServiceProvidedPageable(this.customerName, this.month, this.page, this.sizeOfListCustomer)
    .subscribe( response => {
        this.serviceProvidedList = response['content'];
        this.pages = new Array(response['totalPages']);

        console.log("conteudo lista " + this.serviceProvidedList);
        console.log("pagina " + this.pages.length);
    })

  }

  addServiceProvided(){
    this.router.navigate(['/service-provided/form']);
  }
 
  clearSearch(){
    this.emptyList = true;
    this.month = 0;
    this.customerName = "";
  }

  onSubmit(){
    console.log("submit");
      this.getServiceProvidedPageable();
      if(this.serviceProvidedList == null || this.serviceProvidedList.length == 0){
        this.searchMessageReturn = "Nenhum registro encontrado.";
        this.emptyList = true;
        console.log("empty");
      }else{
        this.emptyList = false;
        console.log("full");
      }
  }
}
