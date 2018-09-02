import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderblotterComponent } from './orderblotter.component';

describe('OrderblotterComponent', () => {
  let component: OrderblotterComponent;
  let fixture: ComponentFixture<OrderblotterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OrderblotterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OrderblotterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
