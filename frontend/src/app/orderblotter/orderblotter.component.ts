import { Component, OnInit } from '@angular/core';
import {ApiService} from '../api.service';
@Component({
  selector: 'app-orderblotter',
  templateUrl: './orderblotter.component.html',
  styleUrls: ['./orderblotter.component.css']
})
export class OrderblotterComponent implements OnInit {
  orders = [];
  errors = {};

  constructor( private apiService: ApiService ) { }

  ngOnInit() {
    let orderblotterUrl = this.apiService.apis.order_blotter.url;
    let queryParams = {
      "blotter_type": "order"
    }
    this.apiService.getRequest(orderblotterUrl, queryParams).subscribe(
      (data) => {
        this.orders  = data["orders"];
        console.log(this.orders);
      },
      (error) => {
        console.log(error);
        this.errors = error;
      }
    );
  }

}
