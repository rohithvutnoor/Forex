import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api.service'

@Component({
  selector: 'app-currency-pair-prices',
  templateUrl: './currency-pair-prices.component.html',
  styleUrls: ['./currency-pair-prices.component.css']
})
export class CurrencyPairPricesComponent implements OnInit {

  public response:any;
  public errors:any;
  public isBusy = false;
  public hasFailed = false;
  public showInputErrors = false;
  
  constructor(
    private apiService: ApiService
  ) { }

  ngOnInit() {
    this.isBusy = true;
    this.hasFailed = false;
    let currencyPairUrl = this.apiService.apis.currency_pair_prices.url;
    let queryParams = {"date": Date.now().toString()};
    this.apiService.getRequest(currencyPairUrl, queryParams).subscribe(
      (data:any) => {
        this.response = data;
      },
      (error:any) => {
        this.isBusy = false;
        this.hasFailed = true;
        this.errors = error;
      }
    )
  }
}
