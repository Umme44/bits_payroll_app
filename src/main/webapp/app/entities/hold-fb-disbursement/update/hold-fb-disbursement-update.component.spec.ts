import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { HoldFbDisbursementFormService } from './hold-fb-disbursement-form.service';
import { HoldFbDisbursementService } from '../service/hold-fb-disbursement.service';
import { IHoldFbDisbursement } from '../hold-fb-disbursement.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IFestivalBonusDetails } from 'app/entities/festival-bonus-details/festival-bonus-details.model';
import { FestivalBonusDetailsService } from 'app/entities/festival-bonus-details/service/festival-bonus-details.service';

import { HoldFbDisbursementUpdateComponent } from './hold-fb-disbursement-update.component';

describe('HoldFbDisbursement Management Update Component', () => {
  let comp: HoldFbDisbursementUpdateComponent;
  let fixture: ComponentFixture<HoldFbDisbursementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let holdFbDisbursementFormService: HoldFbDisbursementFormService;
  let holdFbDisbursementService: HoldFbDisbursementService;
  let userService: UserService;
  let festivalBonusDetailsService: FestivalBonusDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [HoldFbDisbursementUpdateComponent],
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
      .overrideTemplate(HoldFbDisbursementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HoldFbDisbursementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    holdFbDisbursementFormService = TestBed.inject(HoldFbDisbursementFormService);
    holdFbDisbursementService = TestBed.inject(HoldFbDisbursementService);
    userService = TestBed.inject(UserService);
    festivalBonusDetailsService = TestBed.inject(FestivalBonusDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const holdFbDisbursement: IHoldFbDisbursement = { id: 456 };
      const disbursedBy: IUser = { id: 62654 };
      holdFbDisbursement.disbursedBy = disbursedBy;

      const userCollection: IUser[] = [{ id: 94476 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [disbursedBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ holdFbDisbursement });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FestivalBonusDetails query and add missing value', () => {
      const holdFbDisbursement: IHoldFbDisbursement = { id: 456 };
      const festivalBonusDetail: IFestivalBonusDetails = { id: 99576 };
      holdFbDisbursement.festivalBonusDetail = festivalBonusDetail;

      const festivalBonusDetailsCollection: IFestivalBonusDetails[] = [{ id: 23955 }];
      jest.spyOn(festivalBonusDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: festivalBonusDetailsCollection })));
      const additionalFestivalBonusDetails = [festivalBonusDetail];
      const expectedCollection: IFestivalBonusDetails[] = [...additionalFestivalBonusDetails, ...festivalBonusDetailsCollection];
      jest.spyOn(festivalBonusDetailsService, 'addFestivalBonusDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ holdFbDisbursement });
      comp.ngOnInit();

      expect(festivalBonusDetailsService.query).toHaveBeenCalled();
      expect(festivalBonusDetailsService.addFestivalBonusDetailsToCollectionIfMissing).toHaveBeenCalledWith(
        festivalBonusDetailsCollection,
        ...additionalFestivalBonusDetails.map(expect.objectContaining)
      );
      expect(comp.festivalBonusDetailsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const holdFbDisbursement: IHoldFbDisbursement = { id: 456 };
      const disbursedBy: IUser = { id: 83029 };
      holdFbDisbursement.disbursedBy = disbursedBy;
      const festivalBonusDetail: IFestivalBonusDetails = { id: 93204 };
      holdFbDisbursement.festivalBonusDetail = festivalBonusDetail;

      activatedRoute.data = of({ holdFbDisbursement });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(disbursedBy);
      expect(comp.festivalBonusDetailsSharedCollection).toContain(festivalBonusDetail);
      expect(comp.holdFbDisbursement).toEqual(holdFbDisbursement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHoldFbDisbursement>>();
      const holdFbDisbursement = { id: 123 };
      jest.spyOn(holdFbDisbursementFormService, 'getHoldFbDisbursement').mockReturnValue(holdFbDisbursement);
      jest.spyOn(holdFbDisbursementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ holdFbDisbursement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: holdFbDisbursement }));
      saveSubject.complete();

      // THEN
      expect(holdFbDisbursementFormService.getHoldFbDisbursement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(holdFbDisbursementService.update).toHaveBeenCalledWith(expect.objectContaining(holdFbDisbursement));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHoldFbDisbursement>>();
      const holdFbDisbursement = { id: 123 };
      jest.spyOn(holdFbDisbursementFormService, 'getHoldFbDisbursement').mockReturnValue({ id: null });
      jest.spyOn(holdFbDisbursementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ holdFbDisbursement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: holdFbDisbursement }));
      saveSubject.complete();

      // THEN
      expect(holdFbDisbursementFormService.getHoldFbDisbursement).toHaveBeenCalled();
      expect(holdFbDisbursementService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHoldFbDisbursement>>();
      const holdFbDisbursement = { id: 123 };
      jest.spyOn(holdFbDisbursementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ holdFbDisbursement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(holdFbDisbursementService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFestivalBonusDetails', () => {
      it('Should forward to festivalBonusDetailsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(festivalBonusDetailsService, 'compareFestivalBonusDetails');
        comp.compareFestivalBonusDetails(entity, entity2);
        expect(festivalBonusDetailsService.compareFestivalBonusDetails).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
