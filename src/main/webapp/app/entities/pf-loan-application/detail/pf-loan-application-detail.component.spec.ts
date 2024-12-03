import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PfLoanApplicationDetailComponent } from './pf-loan-application-detail.component';

describe('PfLoanApplication Management Detail Component', () => {
  let comp: PfLoanApplicationDetailComponent;
  let fixture: ComponentFixture<PfLoanApplicationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PfLoanApplicationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pfLoanApplication: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PfLoanApplicationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PfLoanApplicationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pfLoanApplication on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pfLoanApplication).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
