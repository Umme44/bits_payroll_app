import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EmployeeSalaryTempDataDetailComponent } from './employee-salary-temp-data-detail.component';

describe('EmployeeSalaryTempData Management Detail Component', () => {
  let comp: EmployeeSalaryTempDataDetailComponent;
  let fixture: ComponentFixture<EmployeeSalaryTempDataDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EmployeeSalaryTempDataDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ employeeSalaryTempData: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EmployeeSalaryTempDataDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EmployeeSalaryTempDataDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load employeeSalaryTempData on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.employeeSalaryTempData).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
