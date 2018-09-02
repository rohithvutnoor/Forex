import { Component, OnInit } from '@angular/core';
import {ApiService} from '../api.service';
@Component({
  selector: 'app-tradeblotter',
  templateUrl: './tradeblotter.component.html',
  styleUrls: ['./tradeblotter.component.css']
})
export class TradeblotterComponent implements OnInit {

  tradedOrders = [];
  errors = {};

  constructor(private apiService: ApiService) { }

  ngOnInit() {
    let tradeblotterUrl = this.apiService.apis.trade_blotter.url;
    let queryParams = {
      "blotter_type": "trade"
    }
    this.apiService.getRequest(tradeblotterUrl, queryParams).subscribe(
      (data) => {
        this.tradedOrders  = data["orders"];
        console.log(this.tradedOrders);
      },
      (error) => {
        console.log(error);
        this.errors = error;
      }
    );
  }

}
