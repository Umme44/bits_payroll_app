import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WorkingExperienceDetailComponent } from './working-experience-detail.component';

describe('WorkingExperience Management Detail Component', () => {
  let comp: WorkingExperienceDetailComponent;
  let fixture: ComponentFixture<WorkingExperienceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WorkingExperienceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ workingExperience: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(WorkingExperienceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(WorkingExperienceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load workingExperience on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.workingExperience).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
