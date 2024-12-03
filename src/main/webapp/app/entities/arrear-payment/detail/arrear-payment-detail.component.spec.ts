import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ArrearPaymentDetailComponent } from './arrear-payment-detail.component';

describe('ArrearPayment Management Detail Component', () => {
  let comp: ArrearPaymentDetailComponent;
  let fixture: ComponentFixture<ArrearPaymentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ArrearPaymentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ arrearPayment: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ArrearPaymentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ArrearPaymentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load arrearPayment on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.arrearPayment).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
