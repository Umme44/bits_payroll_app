import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PfCollectionFormService } from './pf-collection-form.service';
import { PfCollectionService } from '../service/pf-collection.service';
import { IPfCollection } from '../pf-collection.model';
import { IPfAccount } from 'app/entities/pf-account/pf-account.model';
import { PfAccountService } from 'app/entities/pf-account/service/pf-account.service';

import { PfCollectionUpdateComponent } from './pf-collection-update.component';

describe('PfCollection Management Update Component', () => {
  let comp: PfCollectionUpdateComponent;
  let fixture: ComponentFixture<PfCollectionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pfCollectionFormService: PfCollectionFormService;
  let pfCollectionService: PfCollectionService;
  let pfAccountService: PfAccountService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PfCollectionUpdateComponent],
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
      .overrideTemplate(PfCollectionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PfCollectionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pfCollectionFormService = TestBed.inject(PfCollectionFormService);
    pfCollectionService = TestBed.inject(PfCollectionService);
    pfAccountService = TestBed.inject(PfAccountService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PfAccount query and add missing value', () => {
      const pfCollection: IPfCollection = { id: 456 };
      const pfAccount: IPfAccount = { id: 85122 };
      pfCollection.pfAccount = pfAccount;

      const pfAccountCollection: IPfAccount[] = [{ id: 14128 }];
      jest.spyOn(pfAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: pfAccountCollection })));
      const additionalPfAccounts = [pfAccount];
      const expectedCollection: IPfAccount[] = [...additionalPfAccounts, ...pfAccountCollection];
      jest.spyOn(pfAccountService, 'addPfAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pfCollection });
      comp.ngOnInit();

      expect(pfAccountService.query).toHaveBeenCalled();
      expect(pfAccountService.addPfAccountToCollectionIfMissing).toHaveBeenCalledWith(
        pfAccountCollection,
        ...additionalPfAccounts.map(expect.objectContaining)
      );
      expect(comp.pfAccountsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pfCollection: IPfCollection = { id: 456 };
      const pfAccount: IPfAccount = { id: 88542 };
      pfCollection.pfAccount = pfAccount;

      activatedRoute.data = of({ pfCollection });
      comp.ngOnInit();

      expect(comp.pfAccountsSharedCollection).toContain(pfAccount);
      expect(comp.pfCollection).toEqual(pfCollection);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPfCollection>>();
      const pfCollection = { id: 123 };
      jest.spyOn(pfCollectionFormService, 'getPfCollection').mockReturnValue(pfCollection);
      jest.spyOn(pfCollectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pfCollection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pfCollection }));
      saveSubject.complete();

      // THEN
      expect(pfCollectionFormService.getPfCollection).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pfCollectionService.update).toHaveBeenCalledWith(expect.objectContaining(pfCollection));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPfCollection>>();
      const pfCollection = { id: 123 };
      jest.spyOn(pfCollectionFormService, 'getPfCollection').mockReturnValue({ id: null });
      jest.spyOn(pfCollectionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pfCollection: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pfCollection }));
      saveSubject.complete();

      // THEN
      expect(pfCollectionFormService.getPfCollection).toHaveBeenCalled();
      expect(pfCollectionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPfCollection>>();
      const pfCollection = { id: 123 };
      jest.spyOn(pfCollectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pfCollection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pfCollectionService.update).toHaveBeenCalled();
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
  });
});
