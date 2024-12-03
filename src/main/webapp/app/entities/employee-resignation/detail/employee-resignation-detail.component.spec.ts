import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EmployeeResignationDetailComponent } from './employee-resignation-detail.component';

describe('EmployeeResignation Management Detail Component', () => {
  let comp: EmployeeResignationDetailComponent;
  let fixture: ComponentFixture<EmployeeResignationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EmployeeResignationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ employeeResignation: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EmployeeResignationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EmployeeResignationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load employeeResignation on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.employeeResignation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
