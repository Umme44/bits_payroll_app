import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EventLogFormService } from './event-log-form.service';
import { EventLogService } from '../service/event-log.service';
import { IEventLog } from '../event-log.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { EventLogUpdateComponent } from './event-log-update.component';

describe('EventLog Management Update Component', () => {
  let comp: EventLogUpdateComponent;
  let fixture: ComponentFixture<EventLogUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let eventLogFormService: EventLogFormService;
  let eventLogService: EventLogService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EventLogUpdateComponent],
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
      .overrideTemplate(EventLogUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EventLogUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    eventLogFormService = TestBed.inject(EventLogFormService);
    eventLogService = TestBed.inject(EventLogService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const eventLog: IEventLog = { id: 456 };
      const performedBy: IUser = { id: 88471 };
      eventLog.performedBy = performedBy;

      const userCollection: IUser[] = [{ id: 48847 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [performedBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eventLog });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const eventLog: IEventLog = { id: 456 };
      const performedBy: IUser = { id: 64412 };
      eventLog.performedBy = performedBy;

      activatedRoute.data = of({ eventLog });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(performedBy);
      expect(comp.eventLog).toEqual(eventLog);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEventLog>>();
      const eventLog = { id: 123 };
      jest.spyOn(eventLogFormService, 'getEventLog').mockReturnValue(eventLog);
      jest.spyOn(eventLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eventLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eventLog }));
      saveSubject.complete();

      // THEN
      expect(eventLogFormService.getEventLog).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(eventLogService.update).toHaveBeenCalledWith(expect.objectContaining(eventLog));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEventLog>>();
      const eventLog = { id: 123 };
      jest.spyOn(eventLogFormService, 'getEventLog').mockReturnValue({ id: null });
      jest.spyOn(eventLogService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eventLog: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eventLog }));
      saveSubject.complete();

      // THEN
      expect(eventLogFormService.getEventLog).toHaveBeenCalled();
      expect(eventLogService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEventLog>>();
      const eventLog = { id: 123 };
      jest.spyOn(eventLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eventLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(eventLogService.update).toHaveBeenCalled();
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
  });
});
