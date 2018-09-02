import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-buy',
  templateUrl: './buy.component.html',
  styleUrls: ['./buy.component.css']
})
export class BuyComponent implements OnInit {

  public formGroup: FormGroup;
  public isBusy =false;
  public hasFailed = false;
  public showInputErrors = false;
  currency_pairs = [];
  errors = {};
  order_response = {};

  constructor(
    private apiService: ApiService,
    private formBuilder: FormBuilder
  ) { 
    this.formGroup = this.formBuilder.group ({
      currency_pair_id: ['', Validators.required],
      notional_amount: ['', Validators.required]
    });
  }

  ngOnInit() {
    let currency_pairs_url = this.apiService.apis.currency_pairs.url;
    this.apiService.getRequest(currency_pairs_url, {}).subscribe(
      (data) => {
        this.currency_pairs = data["currency_pairs"];
        for ( var i = 0; i < this.currency_pairs.length; i++ ) {
          var str = this.currency_pairs[i].name;
          var c = str.toString().split('/');
          this.currency_pairs[i].name = c[1] + " TO " + c[0];
        }
      },
      (error) => {
        console.log(error);
        this.errors = error;
      }
    );
  }

  buy() {
    if(this.formGroup.invalid) {
      this.showInputErrors = true;
      return;
    }
    this.isBusy = true;
    this.hasFailed = false;

    let buyUrl = this.apiService.apis.buy.url;
    let body = {
      "currency_pair_id": this.formGroup.get("currency_pair_id").value,
      "notional_amount": this.formGroup.get("notional_amount").value
    }
    this.apiService.postRequest(buyUrl, body).subscribe(
      (data) => {
        this.order_response = data;
      },
      (error) => {
        this.errors = error;
      }
    );
  }

}
