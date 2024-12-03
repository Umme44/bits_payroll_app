import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SalaryDeductionDetailComponent } from './salary-deduction-detail.component';

describe('SalaryDeduction Management Detail Component', () => {
  let comp: SalaryDeductionDetailComponent;
  let fixture: ComponentFixture<SalaryDeductionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SalaryDeductionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ salaryDeduction: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SalaryDeductionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SalaryDeductionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load salaryDeduction on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.salaryDeduction).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
