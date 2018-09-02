import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-sell',
  templateUrl: './sell.component.html',
  styleUrls: ['./sell.component.css']
})
export class SellComponent implements OnInit {
  
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
        this.currency_pairs = data['currency_pairs'];
        for ( var i = 0; i < this.currency_pairs.length; i++ ) {
          var str = this.currency_pairs[i].name;
          var c = str.toString().split('/');
          this.currency_pairs[i].name = c[0] + " TO " + c[1];
        }
      },
      (error) => {
        console.log(error);
        this.errors = error;
      }
    );
  }

  sell() {
    if(this.formGroup.invalid) {
      this.showInputErrors = true;
      return;
    }
    this.isBusy = true;
    this.hasFailed = false;

    let sellUrl = this.apiService.apis.sell.url;
    let body = {
      "currency_pair_id": this.formGroup.get("currency_pair_id").value,
      "notional_amount": this.formGroup.get("notional_amount").value
    }
    this.apiService.postRequest(sellUrl, body).subscribe(
      (data) => {
        this.order_response = data;
      },
      (error) => {
        console.log(error);
        this.errors = error;
      }
    );
  }

}
