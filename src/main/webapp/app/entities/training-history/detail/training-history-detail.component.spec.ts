import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TrainingHistoryDetailComponent } from './training-history-detail.component';

describe('TrainingHistory Management Detail Component', () => {
  let comp: TrainingHistoryDetailComponent;
  let fixture: ComponentFixture<TrainingHistoryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TrainingHistoryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ trainingHistory: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TrainingHistoryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TrainingHistoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load trainingHistory on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.trainingHistory).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
