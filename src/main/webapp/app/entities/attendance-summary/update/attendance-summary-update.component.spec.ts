import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AttendanceSummaryFormService } from './attendance-summary-form.service';
import { AttendanceSummaryService } from '../service/attendance-summary.service';
import { IAttendanceSummary } from '../attendance-summary.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { AttendanceSummaryUpdateComponent } from './attendance-summary-update.component';

describe('AttendanceSummary Management Update Component', () => {
  let comp: AttendanceSummaryUpdateComponent;
  let fixture: ComponentFixture<AttendanceSummaryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let attendanceSummaryFormService: AttendanceSummaryFormService;
  let attendanceSummaryService: AttendanceSummaryService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AttendanceSummaryUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AttendanceSummaryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AttendanceSummaryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    attendanceSummaryFormService = TestBed.inject(AttendanceSummaryFormService);
    attendanceSummaryService = TestBed.inject(AttendanceSummaryService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const attendanceSummary: IAttendanceSummary = { id: 456 };
      const employee: IEmployee = { id: 25712 };
      attendanceSummary.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 71254 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ attendanceSummary });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const attendanceSummary: IAttendanceSummary = { id: 456 };
      const employee: IEmployee = { id: 53562 };
      attendanceSummary.employee = employee;

      activatedRoute.data = of({ attendanceSummary });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.attendanceSummary).toEqual(attendanceSummary);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttendanceSummary>>();
      const attendanceSummary = { id: 123 };
      jest.spyOn(attendanceSummaryFormService, 'getAttendanceSummary').mockReturnValue(attendanceSummary);
      jest.spyOn(attendanceSummaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attendanceSummary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: attendanceSummary }));
      saveSubject.complete();

      // THEN
      expect(attendanceSummaryFormService.getAttendanceSummary).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(attendanceSummaryService.update).toHaveBeenCalledWith(expect.objectContaining(attendanceSummary));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttendanceSummary>>();
      const attendanceSummary = { id: 123 };
      jest.spyOn(attendanceSummaryFormService, 'getAttendanceSummary').mockReturnValue({ id: null });
      jest.spyOn(attendanceSummaryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attendanceSummary: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: attendanceSummary }));
      saveSubject.complete();

      // THEN
      expect(attendanceSummaryFormService.getAttendanceSummary).toHaveBeenCalled();
      expect(attendanceSummaryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttendanceSummary>>();
      const attendanceSummary = { id: 123 };
      jest.spyOn(attendanceSummaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attendanceSummary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(attendanceSummaryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
