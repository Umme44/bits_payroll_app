import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PfLoanFormService } from './pf-loan-form.service';
import { PfLoanService } from '../service/pf-loan.service';
import { IPfLoan } from '../pf-loan.model';
import { IPfLoanApplication } from 'app/entities/pf-loan-application/pf-loan-application.model';
import { PfLoanApplicationService } from 'app/entities/pf-loan-application/service/pf-loan-application.service';
import { IPfAccount } from 'app/entities/pf-account/pf-account.model';
import { PfAccountService } from 'app/entities/pf-account/service/pf-account.service';

import { PfLoanUpdateComponent } from './pf-loan-update.component';

describe('PfLoan Management Update Component', () => {
  let comp: PfLoanUpdateComponent;
  let fixture: ComponentFixture<PfLoanUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pfLoanFormService: PfLoanFormService;
  let pfLoanService: PfLoanService;
  let pfLoanApplicationService: PfLoanApplicationService;
  let pfAccountService: PfAccountService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PfLoanUpdateComponent],
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
      .overrideTemplate(PfLoanUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PfLoanUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pfLoanFormService = TestBed.inject(PfLoanFormService);
    pfLoanService = TestBed.inject(PfLoanService);
    pfLoanApplicationService = TestBed.inject(PfLoanApplicationService);
    pfAccountService = TestBed.inject(PfAccountService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PfLoanApplication query and add missing value', () => {
      const pfLoan: IPfLoan = { id: 456 };
      const pfLoanApplication: IPfLoanApplication = { id: 54016 };
      pfLoan.pfLoanApplication = pfLoanApplication;

      const pfLoanApplicationCollection: IPfLoanApplication[] = [{ id: 43152 }];
      jest.spyOn(pfLoanApplicationService, 'query').mockReturnValue(of(new HttpResponse({ body: pfLoanApplicationCollection })));
      const additionalPfLoanApplications = [pfLoanApplication];
      const expectedCollection: IPfLoanApplication[] = [...additionalPfLoanApplications, ...pfLoanApplicationCollection];
      jest.spyOn(pfLoanApplicationService, 'addPfLoanApplicationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pfLoan });
      comp.ngOnInit();

      expect(pfLoanApplicationService.query).toHaveBeenCalled();
      expect(pfLoanApplicationService.addPfLoanApplicationToCollectionIfMissing).toHaveBeenCalledWith(
        pfLoanApplicationCollection,
        ...additionalPfLoanApplications.map(expect.objectContaining)
      );
      expect(comp.pfLoanApplicationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PfAccount query and add missing value', () => {
      const pfLoan: IPfLoan = { id: 456 };
      const pfAccount: IPfAccount = { id: 30531 };
      pfLoan.pfAccount = pfAccount;

      const pfAccountCollection: IPfAccount[] = [{ id: 63132 }];
      jest.spyOn(pfAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: pfAccountCollection })));
      const additionalPfAccounts = [pfAccount];
      const expectedCollection: IPfAccount[] = [...additionalPfAccounts, ...pfAccountCollection];
      jest.spyOn(pfAccountService, 'addPfAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pfLoan });
      comp.ngOnInit();

      expect(pfAccountService.query).toHaveBeenCalled();
      expect(pfAccountService.addPfAccountToCollectionIfMissing).toHaveBeenCalledWith(
        pfAccountCollection,
        ...additionalPfAccounts.map(expect.objectContaining)
      );
      expect(comp.pfAccountsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pfLoan: IPfLoan = { id: 456 };
      const pfLoanApplication: IPfLoanApplication = { id: 35688 };
      pfLoan.pfLoanApplication = pfLoanApplication;
      const pfAccount: IPfAccount = { id: 87183 };
      pfLoan.pfAccount = pfAccount;

      activatedRoute.data = of({ pfLoan });
      comp.ngOnInit();

      expect(comp.pfLoanApplicationsSharedCollection).toContain(pfLoanApplication);
      expect(comp.pfAccountsSharedCollection).toContain(pfAccount);
      expect(comp.pfLoan).toEqual(pfLoan);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPfLoan>>();
      const pfLoan = { id: 123 };
      jest.spyOn(pfLoanFormService, 'getPfLoan').mockReturnValue(pfLoan);
      jest.spyOn(pfLoanService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pfLoan });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pfLoan }));
      saveSubject.complete();

      // THEN
      expect(pfLoanFormService.getPfLoan).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pfLoanService.update).toHaveBeenCalledWith(expect.objectContaining(pfLoan));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPfLoan>>();
      const pfLoan = { id: 123 };
      jest.spyOn(pfLoanFormService, 'getPfLoan').mockReturnValue({ id: null });
      jest.spyOn(pfLoanService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pfLoan: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pfLoan }));
      saveSubject.complete();

      // THEN
      expect(pfLoanFormService.getPfLoan).toHaveBeenCalled();
      expect(pfLoanService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPfLoan>>();
      const pfLoan = { id: 123 };
      jest.spyOn(pfLoanService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pfLoan });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pfLoanService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePfLoanApplication', () => {
      it('Should forward to pfLoanApplicationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(pfLoanApplicationService, 'comparePfLoanApplication');
        comp.comparePfLoanApplication(entity, entity2);
        expect(pfLoanApplicationService.comparePfLoanApplication).toHaveBeenCalledWith(entity, entity2);
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
