<?php

use Cassandra\Date;

class StudentScheduleTable extends TableControlHelper
{
    const DAY_REPEAT_METHOD = 'day';
    const WEEK_REPEAT_METHOD = 'week';
    const MONTH_REPEAT_METHOD = 'month';
    const YEAR_REPEAT_METHOD = 'year';

    /**
     * @var AccountTableControl
     */
    private $accountTable;

    public function __construct()
    {
        parent::__construct(DatabaseControl::get_schedules_table());
        $this->accountTable = new AccountTableControl();
    }

    /**
     * Add all day schedule work
     *
     * @param integer $uid the user ID
     * @param string $title title of schedule
     * @param string $description description of schedule
     * @param Date $date the time schedule was starting
     * @param boolean $isStudy
     * @param integer $lessonId
     * @param boolean $doesRepeat
     * @param integer $repeatMethod
     * @return integer
     * @throws DatabaseException
     * @throws ValidationException
     */
    public function add_all_day_work_schedule($uid, $title, $description, $date, $isStudy, $lessonId, $doesRepeat, $repeatMethod)
    {
        if (!$this->accountTable->is_uid_exists($uid)) {
            throw new ValidationException('no any account exists by this uid');
        }

        $insertData = [
            'title' => $title, '`description`' => $description,
            '`startDate`' => $date, '`endDate`' => $date,
            'isAllDay' => 1,
            'isStudy' => $isStudy ? 1 : 0, 'lessonId' => $lessonId,
            'doesRepeat' => $doesRepeat ? 1 : 0,
            'repeatMethod' => $repeatMethod, 'uid' => $uid
        ];
    
        $insert = $this->tableControl->insert_query($insertData);
        self::error_handler($this->tableControl);
        return $insert;
    }
 
    /**
     * Add an event by start and end time
     *
     * @param integer $uid the user ID
     * @param string $title title of schedule
     * @param string $description description of schedule
     * @param DateTime $startDate the time schedule was starting
     * @param DateTime $endDate the time schedule was ending
     * @param boolean $isStudy
     * @param integer $lessonId
     * @param boolean $doesRepeat
     * @param integer $repeatMethod
     * @return integer
     * @throws DatabaseException|ValidationException
     */
    public function add_work_schedule($uid, $title, $description, $startDate, $endDate, $isStudy, $lessonId, $doesRepeat, $repeatMethod) {
        if (!$this->accountTable->is_uid_exists($uid)) {
            throw new ValidationException('no any account exists by this uid');
        }

        $insertData = [
            'title' => $title, '`description`' => $description,
            '`startDate`' => $startDate, '`endDate`' => $endDate,
            'isAllDay' => 0, 'doesRepeat' => $doesRepeat ? 1 : 0,
            'isStudy' => $isStudy ? 1 : 0, 'lessonId' => $lessonId,
            'repeatMethod' => $repeatMethod, 'uid' => $uid
        ];
    
        $insert = $this->tableControl->insert_query($insertData);
        self::error_handler($this->tableControl);
        return $insert;
    }

    /**
     * select user's schedules from database
     *
     * @param integer $uid the user id you want to get there information
     * @param array $parameters optional
     * @param integer $page no. of page
     * @param integer $count no. of items per page
     * @return array
     * @throws DatabaseException
     */
    public function select_schedules($uid, $parameters = [], $page = 1, $count = 15) {
        $parameters['uid'] = $uid;
        $select_result = $this->select_rows_by_condition($parameters, $page, $count);
        $this->check_query_run_result();
        return $select_result;
    }

    /**
     * @param $schedules
     * @return array|mixed
     * @throws DatabaseException
     */
    public function fix_format($schedules)
    {
        $result = [];
        $accountTable = new AccountTableControl();
        foreach ($schedules as $schedule) {
            if (array_key_exists('uid', $schedule)) {
                $schedule['account'] = $accountTable->get_account_by_uid($schedule['uid']);
                unset($schedule['uid']);
            }
            if (array_key_exists('lessonId', $schedule)) {
                $schedule['lesson'] = (new LessonTableControl())->select_row_by_id((int)$schedule['lessonId']);
                unset($schedule['lessonId']);
            }
            array_push($result, $schedule);
        }

        return $result;
    }
}
