import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FinalSettlementFormService } from './final-settlement-form.service';
import { FinalSettlementService } from '../service/final-settlement.service';
import { IFinalSettlement } from '../final-settlement.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { FinalSettlementUpdateComponent } from './final-settlement-update.component';

describe('FinalSettlement Management Update Component', () => {
  let comp: FinalSettlementUpdateComponent;
  let fixture: ComponentFixture<FinalSettlementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let finalSettlementFormService: FinalSettlementFormService;
  let finalSettlementService: FinalSettlementService;
  let employeeService: EmployeeService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FinalSettlementUpdateComponent],
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
      .overrideTemplate(FinalSettlementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FinalSettlementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    finalSettlementFormService = TestBed.inject(FinalSettlementFormService);
    finalSettlementService = TestBed.inject(FinalSettlementService);
    employeeService = TestBed.inject(EmployeeService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const finalSettlement: IFinalSettlement = { id: 456 };
      const employee: IEmployee = { id: 25301 };
      finalSettlement.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 86764 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ finalSettlement });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const finalSettlement: IFinalSettlement = { id: 456 };
      const createdBy: IUser = { id: 50407 };
      finalSettlement.createdBy = createdBy;
      const updatedBy: IUser = { id: 2075 };
      finalSettlement.updatedBy = updatedBy;

      const userCollection: IUser[] = [{ id: 80187 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [createdBy, updatedBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ finalSettlement });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const finalSettlement: IFinalSettlement = { id: 456 };
      const employee: IEmployee = { id: 99885 };
      finalSettlement.employee = employee;
      const createdBy: IUser = { id: 92313 };
      finalSettlement.createdBy = createdBy;
      const updatedBy: IUser = { id: 1646 };
      finalSettlement.updatedBy = updatedBy;

      activatedRoute.data = of({ finalSettlement });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.usersSharedCollection).toContain(updatedBy);
      expect(comp.finalSettlement).toEqual(finalSettlement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFinalSettlement>>();
      const finalSettlement = { id: 123 };
      jest.spyOn(finalSettlementFormService, 'getFinalSettlement').mockReturnValue(finalSettlement);
      jest.spyOn(finalSettlementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ finalSettlement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: finalSettlement }));
      saveSubject.complete();

      // THEN
      expect(finalSettlementFormService.getFinalSettlement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(finalSettlementService.update).toHaveBeenCalledWith(expect.objectContaining(finalSettlement));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFinalSettlement>>();
      const finalSettlement = { id: 123 };
      jest.spyOn(finalSettlementFormService, 'getFinalSettlement').mockReturnValue({ id: null });
      jest.spyOn(finalSettlementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ finalSettlement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: finalSettlement }));
      saveSubject.complete();

      // THEN
      expect(finalSettlementFormService.getFinalSettlement).toHaveBeenCalled();
      expect(finalSettlementService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFinalSettlement>>();
      const finalSettlement = { id: 123 };
      jest.spyOn(finalSettlementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ finalSettlement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(finalSettlementService.update).toHaveBeenCalled();
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

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
