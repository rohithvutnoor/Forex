import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrencyPairPricesComponent } from './currency-pair-prices.component';

describe('CurrencyPairPicesComponent', () => {
  let component: CurrencyPairPricesComponent;
  let fixture: ComponentFixture<CurrencyPairPricesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CurrencyPairPricesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CurrencyPairPricesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
