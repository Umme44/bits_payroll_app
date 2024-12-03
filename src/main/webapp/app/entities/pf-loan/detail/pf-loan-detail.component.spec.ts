import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PfLoanDetailComponent } from './pf-loan-detail.component';

describe('PfLoan Management Detail Component', () => {
  let comp: PfLoanDetailComponent;
  let fixture: ComponentFixture<PfLoanDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PfLoanDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pfLoan: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PfLoanDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PfLoanDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pfLoan on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pfLoan).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
