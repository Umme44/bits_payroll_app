import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IndividualArrearSalaryDetailComponent } from './individual-arrear-salary-detail.component';

describe('IndividualArrearSalary Management Detail Component', () => {
  let comp: IndividualArrearSalaryDetailComponent;
  let fixture: ComponentFixture<IndividualArrearSalaryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IndividualArrearSalaryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ individualArrearSalary: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IndividualArrearSalaryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IndividualArrearSalaryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load individualArrearSalary on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.individualArrearSalary).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
