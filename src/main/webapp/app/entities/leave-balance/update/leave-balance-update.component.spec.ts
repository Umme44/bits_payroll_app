import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LeaveBalanceFormService } from './leave-balance-form.service';
import { LeaveBalanceService } from '../service/leave-balance.service';
import { ILeaveBalance } from '../leave-balance.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { LeaveBalanceUpdateComponent } from './leave-balance-update.component';

describe('LeaveBalance Management Update Component', () => {
  let comp: LeaveBalanceUpdateComponent;
  let fixture: ComponentFixture<LeaveBalanceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let leaveBalanceFormService: LeaveBalanceFormService;
  let leaveBalanceService: LeaveBalanceService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LeaveBalanceUpdateComponent],
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
      .overrideTemplate(LeaveBalanceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LeaveBalanceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    leaveBalanceFormService = TestBed.inject(LeaveBalanceFormService);
    leaveBalanceService = TestBed.inject(LeaveBalanceService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const leaveBalance: ILeaveBalance = { id: 456 };
      const employee: IEmployee = { id: 23248 };
      leaveBalance.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 86993 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaveBalance });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const leaveBalance: ILeaveBalance = { id: 456 };
      const employee: IEmployee = { id: 13550 };
      leaveBalance.employee = employee;

      activatedRoute.data = of({ leaveBalance });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.leaveBalance).toEqual(leaveBalance);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILeaveBalance>>();
      const leaveBalance = { id: 123 };
      jest.spyOn(leaveBalanceFormService, 'getLeaveBalance').mockReturnValue(leaveBalance);
      jest.spyOn(leaveBalanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaveBalance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leaveBalance }));
      saveSubject.complete();

      // THEN
      expect(leaveBalanceFormService.getLeaveBalance).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(leaveBalanceService.update).toHaveBeenCalledWith(expect.objectContaining(leaveBalance));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILeaveBalance>>();
      const leaveBalance = { id: 123 };
      jest.spyOn(leaveBalanceFormService, 'getLeaveBalance').mockReturnValue({ id: null });
      jest.spyOn(leaveBalanceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaveBalance: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leaveBalance }));
      saveSubject.complete();

      // THEN
      expect(leaveBalanceFormService.getLeaveBalance).toHaveBeenCalled();
      expect(leaveBalanceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILeaveBalance>>();
      const leaveBalance = { id: 123 };
      jest.spyOn(leaveBalanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaveBalance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(leaveBalanceService.update).toHaveBeenCalled();
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
