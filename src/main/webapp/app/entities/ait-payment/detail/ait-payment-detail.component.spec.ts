import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AitPaymentDetailComponent } from './ait-payment-detail.component';

describe('AitPayment Management Detail Component', () => {
  let comp: AitPaymentDetailComponent;
  let fixture: ComponentFixture<AitPaymentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AitPaymentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ aitPayment: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AitPaymentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AitPaymentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load aitPayment on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.aitPayment).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
