import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AttendanceEntryFormService } from './attendance-entry-form.service';
import { AttendanceEntryService } from '../service/attendance-entry.service';
import { IAttendanceEntry } from '../attendance-entry.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { AttendanceEntryUpdateComponent } from './attendance-entry-update.component';

describe('AttendanceEntry Management Update Component', () => {
  let comp: AttendanceEntryUpdateComponent;
  let fixture: ComponentFixture<AttendanceEntryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let attendanceEntryFormService: AttendanceEntryFormService;
  let attendanceEntryService: AttendanceEntryService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AttendanceEntryUpdateComponent],
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
      .overrideTemplate(AttendanceEntryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AttendanceEntryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    attendanceEntryFormService = TestBed.inject(AttendanceEntryFormService);
    attendanceEntryService = TestBed.inject(AttendanceEntryService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const attendanceEntry: IAttendanceEntry = { id: 456 };
      const employee: IEmployee = { id: 60759 };
      attendanceEntry.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 8063 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ attendanceEntry });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const attendanceEntry: IAttendanceEntry = { id: 456 };
      const employee: IEmployee = { id: 34690 };
      attendanceEntry.employee = employee;

      activatedRoute.data = of({ attendanceEntry });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.attendanceEntry).toEqual(attendanceEntry);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttendanceEntry>>();
      const attendanceEntry = { id: 123 };
      jest.spyOn(attendanceEntryFormService, 'getAttendanceEntry').mockReturnValue(attendanceEntry);
      jest.spyOn(attendanceEntryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attendanceEntry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: attendanceEntry }));
      saveSubject.complete();

      // THEN
      expect(attendanceEntryFormService.getAttendanceEntry).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(attendanceEntryService.update).toHaveBeenCalledWith(expect.objectContaining(attendanceEntry));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttendanceEntry>>();
      const attendanceEntry = { id: 123 };
      jest.spyOn(attendanceEntryFormService, 'getAttendanceEntry').mockReturnValue({ id: null });
      jest.spyOn(attendanceEntryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attendanceEntry: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: attendanceEntry }));
      saveSubject.complete();

      // THEN
      expect(attendanceEntryFormService.getAttendanceEntry).toHaveBeenCalled();
      expect(attendanceEntryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttendanceEntry>>();
      const attendanceEntry = { id: 123 };
      jest.spyOn(attendanceEntryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attendanceEntry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(attendanceEntryService.update).toHaveBeenCalled();
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
