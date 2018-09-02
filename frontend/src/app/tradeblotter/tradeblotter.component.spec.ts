import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TradeblotterComponent } from './tradeblotter.component';

describe('TradeblotterComponent', () => {
  let component: TradeblotterComponent;
  let fixture: ComponentFixture<TradeblotterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TradeblotterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TradeblotterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
