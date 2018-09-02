import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-positions',
  templateUrl: './positions.component.html',
  styleUrls: ['./positions.component.css']
})
export class PositionsComponent implements OnInit {

  public formGroup: FormGroup;
  public isBusy =false;
  public hasFailed = false;
  public showInputErrors = false;
  public currentDate = new Date();
  response:any;
  errors:any;
  currency_pairs = [];
  recivable = 0;
  payable = 0;

  constructor(
    private apiService: ApiService,
    private formBuilder: FormBuilder
  ) { 
    this.formGroup = formBuilder.group({
      currencyPairId : ['', Validators.required],
      valueDate : ['', Validators.required] 
    });
  }

  ngOnInit() {
    let currency_pairs_url = this.apiService.apis.currency_pairs.url;
    this.apiService.getRequest(currency_pairs_url, {}).subscribe(
      (data) => {
        this.currency_pairs = data["currency_pairs"];
      },
      (error) => {
        console.log(error);
        this.errors = error;
      }
    );
  }

  positions() {
    if(this.formGroup.invalid) {
      this.showInputErrors = true;
      return;
    }
    this.isBusy = true;
    this.hasFailed = false;
    let date = new Date(this.formGroup.get("valueDate").value);
    let positionsUrl = this.apiService.apis.positions.url;
    let queryParams = {
      "currency_pair_id": this.formGroup.get("currencyPairId").value,
      "ordered_date": date.getTime()
    }

    this.apiService.getRequest(positionsUrl, queryParams).subscribe(
      (data) => {
        this.response = data;
        this.isBusy = false;
        let payableOrRecivable = Number(this.response.totla_buy_orders_amount) 
            - Number(this.response.totla_sell_orders_amount);
        if(payableOrRecivable > -1) {
          this.recivable = payableOrRecivable;
          this.payable = 0;
        } else {
          this.payable = -payableOrRecivable;
          this.recivable = 0;
        }
      },
      (error) => {
        this.errors = error;
        console.log(this.errors);
        this.isBusy = false;
        this.hasFailed = true;
        this.errors = error;
      }
    );
  }

}
