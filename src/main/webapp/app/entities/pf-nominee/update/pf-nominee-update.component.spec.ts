import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PfNomineeFormService } from './pf-nominee-form.service';
import { PfNomineeService } from '../service/pf-nominee.service';
import { IPfNominee } from '../pf-nominee.model';
import { IPfAccount } from 'app/entities/pf-account/pf-account.model';
import { PfAccountService } from 'app/entities/pf-account/service/pf-account.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { PfNomineeUpdateComponent } from './pf-nominee-update.component';

describe('PfNominee Management Update Component', () => {
  let comp: PfNomineeUpdateComponent;
  let fixture: ComponentFixture<PfNomineeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pfNomineeFormService: PfNomineeFormService;
  let pfNomineeService: PfNomineeService;
  let pfAccountService: PfAccountService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PfNomineeUpdateComponent],
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
      .overrideTemplate(PfNomineeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PfNomineeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pfNomineeFormService = TestBed.inject(PfNomineeFormService);
    pfNomineeService = TestBed.inject(PfNomineeService);
    pfAccountService = TestBed.inject(PfAccountService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PfAccount query and add missing value', () => {
      const pfNominee: IPfNominee = { id: 456 };
      const pfAccount: IPfAccount = { id: 53104 };
      pfNominee.pfAccount = pfAccount;

      const pfAccountCollection: IPfAccount[] = [{ id: 28961 }];
      jest.spyOn(pfAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: pfAccountCollection })));
      const additionalPfAccounts = [pfAccount];
      const expectedCollection: IPfAccount[] = [...additionalPfAccounts, ...pfAccountCollection];
      jest.spyOn(pfAccountService, 'addPfAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pfNominee });
      comp.ngOnInit();

      expect(pfAccountService.query).toHaveBeenCalled();
      expect(pfAccountService.addPfAccountToCollectionIfMissing).toHaveBeenCalledWith(
        pfAccountCollection,
        ...additionalPfAccounts.map(expect.objectContaining)
      );
      expect(comp.pfAccountsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const pfNominee: IPfNominee = { id: 456 };
      const pfWitness: IEmployee = { id: 17857 };
      pfNominee.pfWitness = pfWitness;
      const approvedBy: IEmployee = { id: 36137 };
      pfNominee.approvedBy = approvedBy;

      const employeeCollection: IEmployee[] = [{ id: 18642 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [pfWitness, approvedBy];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pfNominee });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pfNominee: IPfNominee = { id: 456 };
      const pfAccount: IPfAccount = { id: 4591 };
      pfNominee.pfAccount = pfAccount;
      const pfWitness: IEmployee = { id: 18035 };
      pfNominee.pfWitness = pfWitness;
      const approvedBy: IEmployee = { id: 26866 };
      pfNominee.approvedBy = approvedBy;

      activatedRoute.data = of({ pfNominee });
      comp.ngOnInit();

      expect(comp.pfAccountsSharedCollection).toContain(pfAccount);
      expect(comp.employeesSharedCollection).toContain(pfWitness);
      expect(comp.employeesSharedCollection).toContain(approvedBy);
      expect(comp.pfNominee).toEqual(pfNominee);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPfNominee>>();
      const pfNominee = { id: 123 };
      jest.spyOn(pfNomineeFormService, 'getPfNominee').mockReturnValue(pfNominee);
      jest.spyOn(pfNomineeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pfNominee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pfNominee }));
      saveSubject.complete();

      // THEN
      expect(pfNomineeFormService.getPfNominee).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pfNomineeService.update).toHaveBeenCalledWith(expect.objectContaining(pfNominee));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPfNominee>>();
      const pfNominee = { id: 123 };
      jest.spyOn(pfNomineeFormService, 'getPfNominee').mockReturnValue({ id: null });
      jest.spyOn(pfNomineeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pfNominee: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pfNominee }));
      saveSubject.complete();

      // THEN
      expect(pfNomineeFormService.getPfNominee).toHaveBeenCalled();
      expect(pfNomineeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPfNominee>>();
      const pfNominee = { id: 123 };
      jest.spyOn(pfNomineeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pfNominee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pfNomineeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePfAccount', () => {
      it('Should forward to pfAccountService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(pfAccountService, 'comparePfAccount');
        comp.comparePfAccount(entity, entity2);
        expect(pfAccountService.comparePfAccount).toHaveBeenCalledWith(entity, entity2);
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
  });
});
