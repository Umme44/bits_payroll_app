import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PfLoanApplicationFormService } from './pf-loan-application-form.service';
import { PfLoanApplicationService } from '../service/pf-loan-application.service';
import { IPfLoanApplication } from '../pf-loan-application.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IPfAccount } from 'app/entities/pf-account/pf-account.model';
import { PfAccountService } from 'app/entities/pf-account/service/pf-account.service';

import { PfLoanApplicationUpdateComponent } from './pf-loan-application-update.component';

describe('PfLoanApplication Management Update Component', () => {
  let comp: PfLoanApplicationUpdateComponent;
  let fixture: ComponentFixture<PfLoanApplicationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pfLoanApplicationFormService: PfLoanApplicationFormService;
  let pfLoanApplicationService: PfLoanApplicationService;
  let employeeService: EmployeeService;
  let pfAccountService: PfAccountService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PfLoanApplicationUpdateComponent],
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
      .overrideTemplate(PfLoanApplicationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PfLoanApplicationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pfLoanApplicationFormService = TestBed.inject(PfLoanApplicationFormService);
    pfLoanApplicationService = TestBed.inject(PfLoanApplicationService);
    employeeService = TestBed.inject(EmployeeService);
    pfAccountService = TestBed.inject(PfAccountService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const pfLoanApplication: IPfLoanApplication = { id: 456 };
      const recommendedBy: IEmployee = { id: 40966 };
      pfLoanApplication.recommendedBy = recommendedBy;
      const approvedBy: IEmployee = { id: 95783 };
      pfLoanApplication.approvedBy = approvedBy;
      const rejectedBy: IEmployee = { id: 46231 };
      pfLoanApplication.rejectedBy = rejectedBy;

      const employeeCollection: IEmployee[] = [{ id: 7949 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [recommendedBy, approvedBy, rejectedBy];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pfLoanApplication });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PfAccount query and add missing value', () => {
      const pfLoanApplication: IPfLoanApplication = { id: 456 };
      const pfAccount: IPfAccount = { id: 319 };
      pfLoanApplication.pfAccount = pfAccount;

      const pfAccountCollection: IPfAccount[] = [{ id: 98232 }];
      jest.spyOn(pfAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: pfAccountCollection })));
      const additionalPfAccounts = [pfAccount];
      const expectedCollection: IPfAccount[] = [...additionalPfAccounts, ...pfAccountCollection];
      jest.spyOn(pfAccountService, 'addPfAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pfLoanApplication });
      comp.ngOnInit();

      expect(pfAccountService.query).toHaveBeenCalled();
      expect(pfAccountService.addPfAccountToCollectionIfMissing).toHaveBeenCalledWith(
        pfAccountCollection,
        ...additionalPfAccounts.map(expect.objectContaining)
      );
      expect(comp.pfAccountsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pfLoanApplication: IPfLoanApplication = { id: 456 };
      const recommendedBy: IEmployee = { id: 99409 };
      pfLoanApplication.recommendedBy = recommendedBy;
      const approvedBy: IEmployee = { id: 43691 };
      pfLoanApplication.approvedBy = approvedBy;
      const rejectedBy: IEmployee = { id: 61985 };
      pfLoanApplication.rejectedBy = rejectedBy;
      const pfAccount: IPfAccount = { id: 59013 };
      pfLoanApplication.pfAccount = pfAccount;

      activatedRoute.data = of({ pfLoanApplication });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(recommendedBy);
      expect(comp.employeesSharedCollection).toContain(approvedBy);
      expect(comp.employeesSharedCollection).toContain(rejectedBy);
      expect(comp.pfAccountsSharedCollection).toContain(pfAccount);
      expect(comp.pfLoanApplication).toEqual(pfLoanApplication);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPfLoanApplication>>();
      const pfLoanApplication = { id: 123 };
      jest.spyOn(pfLoanApplicationFormService, 'getPfLoanApplication').mockReturnValue(pfLoanApplication);
      jest.spyOn(pfLoanApplicationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pfLoanApplication });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pfLoanApplication }));
      saveSubject.complete();

      // THEN
      expect(pfLoanApplicationFormService.getPfLoanApplication).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pfLoanApplicationService.update).toHaveBeenCalledWith(expect.objectContaining(pfLoanApplication));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPfLoanApplication>>();
      const pfLoanApplication = { id: 123 };
      jest.spyOn(pfLoanApplicationFormService, 'getPfLoanApplication').mockReturnValue({ id: null });
      jest.spyOn(pfLoanApplicationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pfLoanApplication: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pfLoanApplication }));
      saveSubject.complete();

      // THEN
      expect(pfLoanApplicationFormService.getPfLoanApplication).toHaveBeenCalled();
      expect(pfLoanApplicationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPfLoanApplication>>();
      const pfLoanApplication = { id: 123 };
      jest.spyOn(pfLoanApplicationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pfLoanApplication });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pfLoanApplicationService.update).toHaveBeenCalled();
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

    describe('comparePfAccount', () => {
      it('Should forward to pfAccountService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(pfAccountService, 'comparePfAccount');
        comp.comparePfAccount(entity, entity2);
        expect(pfAccountService.comparePfAccount).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
