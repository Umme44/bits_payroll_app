import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProcReqMasterFormService } from './proc-req-master-form.service';
import { ProcReqService } from '../service/proc-req.service';
import { IProcReq } from '../proc-req.model';
import { IItemInformation } from 'app/entities/item-information/item-information.model';
import { ItemInformationService } from 'app/entities/item-information/service/item-information.service';
import { IProcReqMaster } from 'app/entities/proc-req-master/proc-req-master.model';
import { ProcReqMasterService } from 'app/entities/proc-req-master/service/proc-req-master.service';

import { ProcReqUpdateComponent } from './proc-req-update.component';

describe('ProcReq Management Update Component', () => {
  let comp: ProcReqUpdateComponent;
  let fixture: ComponentFixture<ProcReqUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let procReqFormService: ProcReqMasterFormService;
  let procReqService: ProcReqService;
  let itemInformationService: ItemInformationService;
  let procReqMasterService: ProcReqMasterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProcReqUpdateComponent],
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
      .overrideTemplate(ProcReqUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProcReqUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    procReqFormService = TestBed.inject(ProcReqMasterFormService);
    procReqService = TestBed.inject(ProcReqService);
    itemInformationService = TestBed.inject(ItemInformationService);
    procReqMasterService = TestBed.inject(ProcReqMasterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ItemInformation query and add missing value', () => {
      const procReq: IProcReq = { id: 456 };
      const itemInformation: IItemInformation = { id: 30820 };
      procReq.itemInformation = itemInformation;

      const itemInformationCollection: IItemInformation[] = [{ id: 19021 }];
      jest.spyOn(itemInformationService, 'query').mockReturnValue(of(new HttpResponse({ body: itemInformationCollection })));
      const additionalItemInformations = [itemInformation];
      const expectedCollection: IItemInformation[] = [...additionalItemInformations, ...itemInformationCollection];
      jest.spyOn(itemInformationService, 'addItemInformationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ procReq });
      comp.ngOnInit();

      expect(itemInformationService.query).toHaveBeenCalled();
      expect(itemInformationService.addItemInformationToCollectionIfMissing).toHaveBeenCalledWith(
        itemInformationCollection,
        ...additionalItemInformations.map(expect.objectContaining)
      );
      expect(comp.itemInformationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ProcReqMaster query and add missing value', () => {
      const procReq: IProcReq = { id: 456 };
      const procReqMaster: IProcReqMaster = { id: 83350 };
      procReq.procReqMaster = procReqMaster;

      const procReqMasterCollection: IProcReqMaster[] = [{ id: 94684 }];
      jest.spyOn(procReqMasterService, 'query').mockReturnValue(of(new HttpResponse({ body: procReqMasterCollection })));
      const additionalProcReqMasters = [procReqMaster];
      const expectedCollection: IProcReqMaster[] = [...additionalProcReqMasters, ...procReqMasterCollection];
      jest.spyOn(procReqMasterService, 'addProcReqMasterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ procReq });
      comp.ngOnInit();

      expect(procReqMasterService.query).toHaveBeenCalled();
      expect(procReqMasterService.addProcReqMasterToCollectionIfMissing).toHaveBeenCalledWith(
        procReqMasterCollection,
        ...additionalProcReqMasters.map(expect.objectContaining)
      );
      expect(comp.procReqMastersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const procReq: IProcReq = { id: 456 };
      const itemInformation: IItemInformation = { id: 52177 };
      procReq.itemInformation = itemInformation;
      const procReqMaster: IProcReqMaster = { id: 93256 };
      procReq.procReqMaster = procReqMaster;

      activatedRoute.data = of({ procReq });
      comp.ngOnInit();

      expect(comp.itemInformationsSharedCollection).toContain(itemInformation);
      expect(comp.procReqMastersSharedCollection).toContain(procReqMaster);
      expect(comp.procReq).toEqual(procReq);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProcReq>>();
      const procReq = { id: 123 };
      jest.spyOn(procReqFormService, 'getProcReq').mockReturnValue(procReq);
      jest.spyOn(procReqService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ procReq });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: procReq }));
      saveSubject.complete();

      // THEN
      expect(procReqFormService.getProcReq).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(procReqService.update).toHaveBeenCalledWith(expect.objectContaining(procReq));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProcReq>>();
      const procReq = { id: 123 };
      jest.spyOn(procReqFormService, 'getProcReq').mockReturnValue({ id: null });
      jest.spyOn(procReqService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ procReq: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: procReq }));
      saveSubject.complete();

      // THEN
      expect(procReqFormService.getProcReq).toHaveBeenCalled();
      expect(procReqService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProcReq>>();
      const procReq = { id: 123 };
      jest.spyOn(procReqService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ procReq });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(procReqService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareItemInformation', () => {
      it('Should forward to itemInformationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(itemInformationService, 'compareItemInformation');
        comp.compareItemInformation(entity, entity2);
        expect(itemInformationService.compareItemInformation).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProcReqMaster', () => {
      it('Should forward to procReqMasterService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(procReqMasterService, 'compareProcReqMaster');
        comp.compareProcReqMaster(entity, entity2);
        expect(procReqMasterService.compareProcReqMaster).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
