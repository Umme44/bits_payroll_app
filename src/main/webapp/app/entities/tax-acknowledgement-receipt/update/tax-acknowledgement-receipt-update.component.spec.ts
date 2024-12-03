import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TaxAcknowledgementReceiptFormService } from './tax-acknowledgement-receipt-form.service';
import { TaxAcknowledgementReceiptService } from '../service/tax-acknowledgement-receipt.service';
import { ITaxAcknowledgementReceipt } from '../tax-acknowledgement-receipt.model';
import { IAitConfig } from 'app/entities/ait-config/ait-config.model';
import { AitConfigService } from 'app/entities/ait-config/service/ait-config.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { TaxAcknowledgementReceiptUpdateComponent } from './tax-acknowledgement-receipt-update.component';

describe('TaxAcknowledgementReceipt Management Update Component', () => {
  let comp: TaxAcknowledgementReceiptUpdateComponent;
  let fixture: ComponentFixture<TaxAcknowledgementReceiptUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let taxAcknowledgementReceiptFormService: TaxAcknowledgementReceiptFormService;
  let taxAcknowledgementReceiptService: TaxAcknowledgementReceiptService;
  let aitConfigService: AitConfigService;
  let employeeService: EmployeeService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TaxAcknowledgementReceiptUpdateComponent],
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
      .overrideTemplate(TaxAcknowledgementReceiptUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TaxAcknowledgementReceiptUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    taxAcknowledgementReceiptFormService = TestBed.inject(TaxAcknowledgementReceiptFormService);
    taxAcknowledgementReceiptService = TestBed.inject(TaxAcknowledgementReceiptService);
    aitConfigService = TestBed.inject(AitConfigService);
    employeeService = TestBed.inject(EmployeeService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AitConfig query and add missing value', () => {
      const taxAcknowledgementReceipt: ITaxAcknowledgementReceipt = { id: 456 };
      const fiscalYear: IAitConfig = { id: 52871 };
      taxAcknowledgementReceipt.fiscalYear = fiscalYear;

      const aitConfigCollection: IAitConfig[] = [{ id: 12753 }];
      jest.spyOn(aitConfigService, 'query').mockReturnValue(of(new HttpResponse({ body: aitConfigCollection })));
      const additionalAitConfigs = [fiscalYear];
      const expectedCollection: IAitConfig[] = [...additionalAitConfigs, ...aitConfigCollection];
      jest.spyOn(aitConfigService, 'addAitConfigToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ taxAcknowledgementReceipt });
      comp.ngOnInit();

      expect(aitConfigService.query).toHaveBeenCalled();
      expect(aitConfigService.addAitConfigToCollectionIfMissing).toHaveBeenCalledWith(
        aitConfigCollection,
        ...additionalAitConfigs.map(expect.objectContaining)
      );
      expect(comp.aitConfigsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const taxAcknowledgementReceipt: ITaxAcknowledgementReceipt = { id: 456 };
      const employee: IEmployee = { id: 40384 };
      taxAcknowledgementReceipt.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 86389 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ taxAcknowledgementReceipt });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const taxAcknowledgementReceipt: ITaxAcknowledgementReceipt = { id: 456 };
      const receivedBy: IUser = { id: 43283 };
      taxAcknowledgementReceipt.receivedBy = receivedBy;
      const createdBy: IUser = { id: 48963 };
      taxAcknowledgementReceipt.createdBy = createdBy;
      const updatedBy: IUser = { id: 95295 };
      taxAcknowledgementReceipt.updatedBy = updatedBy;

      const userCollection: IUser[] = [{ id: 5026 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [receivedBy, createdBy, updatedBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ taxAcknowledgementReceipt });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const taxAcknowledgementReceipt: ITaxAcknowledgementReceipt = { id: 456 };
      const fiscalYear: IAitConfig = { id: 55916 };
      taxAcknowledgementReceipt.fiscalYear = fiscalYear;
      const employee: IEmployee = { id: 45149 };
      taxAcknowledgementReceipt.employee = employee;
      const receivedBy: IUser = { id: 10556 };
      taxAcknowledgementReceipt.receivedBy = receivedBy;
      const createdBy: IUser = { id: 14617 };
      taxAcknowledgementReceipt.createdBy = createdBy;
      const updatedBy: IUser = { id: 41016 };
      taxAcknowledgementReceipt.updatedBy = updatedBy;

      activatedRoute.data = of({ taxAcknowledgementReceipt });
      comp.ngOnInit();

      expect(comp.aitConfigsSharedCollection).toContain(fiscalYear);
      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.usersSharedCollection).toContain(receivedBy);
      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.usersSharedCollection).toContain(updatedBy);
      expect(comp.taxAcknowledgementReceipt).toEqual(taxAcknowledgementReceipt);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITaxAcknowledgementReceipt>>();
      const taxAcknowledgementReceipt = { id: 123 };
      jest.spyOn(taxAcknowledgementReceiptFormService, 'getTaxAcknowledgementReceipt').mockReturnValue(taxAcknowledgementReceipt);
      jest.spyOn(taxAcknowledgementReceiptService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ taxAcknowledgementReceipt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: taxAcknowledgementReceipt }));
      saveSubject.complete();

      // THEN
      expect(taxAcknowledgementReceiptFormService.getTaxAcknowledgementReceipt).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(taxAcknowledgementReceiptService.update).toHaveBeenCalledWith(expect.objectContaining(taxAcknowledgementReceipt));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITaxAcknowledgementReceipt>>();
      const taxAcknowledgementReceipt = { id: 123 };
      jest.spyOn(taxAcknowledgementReceiptFormService, 'getTaxAcknowledgementReceipt').mockReturnValue({ id: null });
      jest.spyOn(taxAcknowledgementReceiptService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ taxAcknowledgementReceipt: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: taxAcknowledgementReceipt }));
      saveSubject.complete();

      // THEN
      expect(taxAcknowledgementReceiptFormService.getTaxAcknowledgementReceipt).toHaveBeenCalled();
      expect(taxAcknowledgementReceiptService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITaxAcknowledgementReceipt>>();
      const taxAcknowledgementReceipt = { id: 123 };
      jest.spyOn(taxAcknowledgementReceiptService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ taxAcknowledgementReceipt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(taxAcknowledgementReceiptService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAitConfig', () => {
      it('Should forward to aitConfigService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(aitConfigService, 'compareAitConfig');
        comp.compareAitConfig(entity, entity2);
        expect(aitConfigService.compareAitConfig).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
