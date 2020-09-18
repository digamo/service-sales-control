import { Component } from '@angular/core';

const getMonth = (idx) => {

  var objDate = new Date();
  objDate.setDate(1);
  objDate.setMonth(idx-1);

  var locale = "en-us",
      month = objDate.toLocaleString(locale, { month: "long" });

    return month;
}

@Component({
  selector: 'app-list-months',
  templateUrl: './list-months.component.html',
  styleUrls: ['./list-months.component.css']
})

export class ListMonthsComponent {

  months = Array(12).fill(0).map((i,idx) => getMonth(idx + 1));

  selectedMonth = 1;
 
}