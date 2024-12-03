import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PfLoanRepaymentDetailComponent } from './pf-loan-repayment-detail.component';

describe('PfLoanRepayment Management Detail Component', () => {
  let comp: PfLoanRepaymentDetailComponent;
  let fixture: ComponentFixture<PfLoanRepaymentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PfLoanRepaymentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pfLoanRepayment: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PfLoanRepaymentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PfLoanRepaymentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pfLoanRepayment on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pfLoanRepayment).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
